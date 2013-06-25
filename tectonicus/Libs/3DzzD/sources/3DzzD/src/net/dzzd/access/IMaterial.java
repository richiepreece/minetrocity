/*
* This file is part of 3DzzD http://dzzd.net/.
*
* Released under LGPL
*
* 3DzzD is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* 3DzzD is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with 3DzzD.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2005 - 2009 Bruno Augier
*/

package net.dzzd.access;

/** 
 *  Used for accessing to a Material.
 *  <p>
 *   Material specify the appareance of a Face3D.
 *  </p>
 *  <br>
 *  Available implemented features are :<br>
 *   - ambiant color 
 *   - diffuse color
 *   - specular color 
 *   - specular power
 *   - specular level
 *   - diffuse color and/or alpha texture.
 *   - diffuse light detail texture.
 *   - global alpha.
 *  <br>
 *
 *  @author Bruno Augier
 *  @version 1.0, 01/01/04
 *  @since 1.0
 *	@see IMaterial
 *	@see ITexture
 */
public interface IMaterial extends ISceneObject
{
	/**
	 * Gets MappingUV to use for diffuse texture color and diffuse light detail texture.
	 *
     * @return current MappingUV or null if none
	 */
	public IMappingUV getMappingUV();

	/**
	 * Sets MappingUV to use for diffuse texture color and diffuse light detail texture.
	 *
     * @param mapping new MappingUV or null to remove
	 */
	public void setMappingUV(IMappingUV mapping);


	/**
	 * Gets ambient color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @return color.
	 */
	public int getAmbientColor();

	/**
	 * Gets diffuse color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @return color.
	 */
	public int getDiffuseColor();

	/**
	 * Gets emissive color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @return color.
	 */
	public int getEmissiveColor();

	/**
	 * Gets self illumination level in unit .
	 *
     * @return self illumination level.
	 */
	public int getSelfIlluminationLevel();	

	/**
	 * Gets specular color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @return color.
	 */
	public int getSpecularColor();

	/**
	 * Gets specular level in unit : 255 <=> 100% .
	 *
     * You can use value greater than 255 to make an object looking very bright. 
     *
     * @return color.
	 */
	public int getSpecularLevel();

	/**
	 * Gets specular power.
	 *
     * Higher value increment the glosiness. 
     *
     * @return color.
	 */
	public int getSpecularPower();

	/**
	 * Gets alpha level value.
	 * 
     *  0=opaque
	 *  Higher value make the object more transparent.
	 *  255=invisible
	 *
     * Higher value increment the glosiness. 
     *
     * @return color.
	 */
	public int getAlphaLevel();

	/**
	 * Sets ambient color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @param color new color.
	 */
	public void setAmbientColor(int color);

	/**
	 * Sets diffuse color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @param color new color.
	 */
	public void setDiffuseColor(int color);

	/**
	 * Sets emissive color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @param color new color.
	 */
	public void setEmissiveColor(int color);	
	
	/**
	 * Sets self illumination level in unit .
	 *
     * @param level new level.
	 */
	public void setSelfIlluminationLevel(int level);	

	/**
	 * Sets specualar color as an 24bit int using the following bitmask RRGGBB.
	 *
     * @param color new color.
	 */
	public void setSpecularColor(int color);

	/**
	 * Sets specular level in unit : 255 <=> 100% .
	 *
     * You can use value greater than 255 to make an object looking very bright. 
     *
     * @param level new level.
	 */
	public void setSpecularLevel(int level);

	/**
	 * Sets specular power.
	 *
     * Higher value increment the glosiness. 
     *
     * @param power new power.
	 */
	public void setSpecularPower(int power);

	/**
	 * Sets alpha level value.
	 * 
     *  0=opaque
	 *  Higher value make the object more transparent.
	 *  255=invisible
	 *
     * Higher value increment the glosiness. 
     *
     * @param alpha new alpha value.
	 */
	public void setAlphaLevel(int alpha);

	/**
	 * Gets alpha flag.
	 *     
     * @return true if this object must use alpha.
	 */
	public boolean getAlphaEnabled();

	/**
	 * Sets alpha flag.
     *
     * @param flag true if this material must use alpha.
	 */
	public void setAlphaEnabled(boolean alphaEnabled);

	/**
	 * Gets a flag that indicate if this Material use alpha channel.
	 * <br>
	 * If this Material has no alpha map or an alpha level==0 than this will return false even if setAlphaEnable has been called with true
     *
     * @return flag true if this material should be rendered with alpha.
	 */
	public boolean isUsingAlpha();

	/**
	 * Gets Texture to use for diffuse color texture.
	 *
     * @return interface to access the Texture or null if none
	 */
	public ITexture getDiffuseTexture();
	
	/**
	 * Gets Texture to use for bump
	 *
     * @return interface to access the Texture or null if none
	 */
	public ITexture getBumpNormalTexture();	
	
	/**
	 * Gets Texture to use for environment
	 *
     * @return interface to access the Texture or null if none
	 */
	public ITexture getEnvTexture();		

	/**
	 * Gets Texture to use for diffuse light detail texture.
	 *
     * @return interface to access the Texture or null if none
	 */
	public ITexture getDiffuseTextureDetail();

	/**
	 * Sets Texture to use for diffuse color texture.
	 *
     * @param texture interface to access the Texture or null if none
	 */
	public void setDiffuseTexture(ITexture texture);
	
	/**
	 * Sets Texture to use for bump
	 *
     * @param texture interface to access the Texture or null if none
	 */
	public void setBumpNormalTexture(ITexture texture);	

	/**
	 * Sets Texture to use for environment
	 *
     * @param texture interface to access the Texture or null if none
	 */
	public void setEnvTexture(ITexture texture);	

	/**
	 * Sets Texture to use for diffuse light detail texture.
	 *
     * @param texture interface to access the Texture or null if none
	 */
	public void setDiffuseTextureDetail(ITexture texture);	

	/**
	 * Sets detail texture factor.
	 *
	 * When using a detail texture this factor will be used to scale mapping coordinate of the main texture.<br>
	 * negative value mean size decrease.<br>
	 * positive value mean size increase.<br>
	 *
	 * value must be given as ln2 factor:<br>
	 *  - 2 mean detail texture is 4 times smaller than main texture.<br>
	 *  - 1 mean detail texture is 2 times smaller than main texture.<br>
	 *    0 mean same size<br>
	 *  + 1 mean detail texture is 2 times bigger than main texture.<br>
	 *  + 2 mean detail texture is 4 times bigger than main texture.<br>
	 * 
	 * <br>
	 * Values can range from -8(scale by 1/256) to +8(scale by 256)
	 *
     * @param diffuseTextureDetailFactor new detail factor
	 */
	public void setDiffuseTextureDetailFactor(int diffuseTextureDetailFactor);

	/**
	 * Gets detail texture factor.
	 *
	 * When using a detail texture this factor will be used to scale mapping coordinate of the main texture.<br>
	 * negative value mean size decrease.<br>
	 * positive value mean size increase.<br>
	 *
	 * value must be given as ln2 factor:<br>
	 *  - 2 mean detail texture is 4 times smaller than main texture.<br>
	 *  - 1 mean detail texture is 2 times smaller than main texture.<br>
	 *    0 mean same size<br>
	 *  + 1 mean detail texture is 2 times bigger than main texture.<br>
	 *  + 2 mean detail texture is 4 times bigger than main texture.<br>
	 * 
	 * <br>
	 * Values can range from -8(scale by 1/256) to +8(scale by 256)
	 *
     * @return detail factor
	 */
    public int getDiffuseTextureDetailFactor();
}